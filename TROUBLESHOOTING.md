# 🔧 Guia de Troubleshooting - Tech Challenge Customer

## 📋 Índice
- [Problemas com GitHub Actions](#problemas-com-github-actions)
- [Problemas com AWS](#problemas-com-aws)
- [Problemas com Terraform](#problemas-com-terraform)
- [Problemas com Kubernetes](#problemas-com-kubernetes)
- [Como ver os logs detalhados](#como-ver-os-logs-detalhados)

---

## 🎯 Problemas com GitHub Actions

### Workflow não mostra informações
**Sintoma:** O workflow roda muito rápido e não mostra o que aconteceu.

**Solução:** 
1. Vá na aba **Actions** do repositório
2. Clique no workflow run específico
3. Clique na aba **Summary** - agora você verá um resumo detalhado com:
   - Status de cada verificação
   - Logs de erros
   - Informações sobre AWS e recursos

### Workflow falha silenciosamente
**Sintoma:** O workflow marca como falha mas não há mensagem clara.

**Solução:**
1. Execute o workflow manualmente com **Debug Mode**:
   - Vá em Actions → CI/CD Customer Service
   - Clique "Run workflow"
   - Marque "Enable debug mode for verbose logging"
   - Execute

2. Verifique a aba **Summary** do workflow run

---

## ☁️ Problemas com AWS

### Credenciais expiradas (AWS Academy)

**Sintoma:** 
```
Error: ExpiredToken: The security token included in the request is expired
```

**Causa:** AWS Academy expira as credenciais a cada ~4 horas.

**Solução:**
1. Acesse [AWS Academy](https://awsacademy.instructure.com/)
2. Inicie/reinicie seu Lab
3. Clique em "AWS Details"
4. Copie as credenciais (Show -> AWS CLI)
5. Atualize os **Secrets** do repositório:
   - Vá em Settings → Secrets and variables → Actions
   - Atualize:
     - `AWS_ACCESS_KEY_ID`
     - `AWS_SECRET_ACCESS_KEY`  
     - `AWS_SESSION_TOKEN`

### ECR Repository não existe

**Sintoma:**
```
Error: RepositoryNotFoundException: The repository with name 'tech-challenge-customer' does not exist
```

**Solução:**
1. Deploy `tech-challenge-infra` primeiro
2. Aguarde o workflow concluir
3. Tente novamente o deploy do customer

### EKS Cluster não existe

**Sintoma:**
```
Error: ResourceNotFoundException: No cluster found for name: tech-challenge-cluster
```

**Solução:**
1. Deploy `tech-challenge-infra` primeiro
2. Aguarde o cluster ficar no estado ACTIVE (~15 min)
3. Tente novamente

---

## 🔧 Problemas com Terraform

### State lock

**Sintoma:**
```
Error: Error acquiring the state lock
```

**Solução:**
1. Aguarde alguns minutos (outro deploy pode estar rodando)
2. Se persistir, force o unlock:
   ```bash
   cd terraform
   terraform force-unlock <LOCK_ID>
   ```

### Identity changed / State corrompido

**Sintoma:**
```
Error: Provider produced inconsistent result after apply
Planning failed: resource.kubernetes_deployment has identity
```

**Solução:**
O workflow já tenta corrigir isso automaticamente. Se persistir:
1. Execute o workflow de **Destroy** primeiro
2. Depois execute o **Deploy** novamente

### Backend S3 não existe

**Sintoma:**
```
Error: Error loading state: BucketNotFound
```

**Solução:**
1. Deploy `tech-challenge-infra` com a opção **run_bootstrap = true**
2. Ou crie manualmente:
   ```bash
   aws s3 mb s3://tech-challenge-tfstate-group240 --region us-east-1
   ```

---

## 🎯 Problemas com Kubernetes

### Pod não inicia (CrashLoopBackOff)

**Sintoma:** Pod fica em status CrashLoopBackOff

**Diagnóstico:**
```bash
# Ver logs do pod
kubectl logs -n tech-challenge deployment/customer-deployment

# Ver eventos
kubectl describe pod -n tech-challenge -l app=customer
```

**Causas comuns:**
1. **Health check falhando:** Verifique se `/api/actuator/health` está respondendo
2. **Variáveis de ambiente:** Verifique se todas as envs necessárias estão configuradas
3. **Banco de dados:** Verifique se o RDS está acessível

### Pod não consegue conectar ao RDS

**Sintoma:** 
```
Connection refused to database
```

**Solução:**
1. Verifique se `tech-challenge-rds` foi deployado
2. Verifique as Security Groups do RDS
3. Verifique se as credenciais estão corretas nos Secrets do GitHub

---

## 📊 Como ver os logs detalhados

### 1. GitHub Actions Summary
Após cada execução do workflow, vá em:
- Actions → Selecione o run → Aba **Summary**

Você verá:
- ✅/❌ Status de cada verificação
- 📋 Logs detalhados de erros
- 📊 Métricas (cobertura, tamanho da imagem, etc.)

### 2. Artifacts
Logs e relatórios são salvos como artifacts:
- `jacoco-report` - Relatório de cobertura de testes
- `app-jar` - JAR buildado

### 3. Debug Mode
Para logs ainda mais detalhados:
1. Run workflow manualmente
2. Marque "Enable debug mode"
3. Execute

Isso habilita `TF_LOG=DEBUG` para Terraform e logs extras.

### 4. Kubectl (se tiver acesso)
```bash
# Configurar acesso ao cluster
aws eks update-kubeconfig --name tech-challenge-cluster --region us-east-1

# Ver pods
kubectl get pods -n tech-challenge

# Ver logs
kubectl logs -n tech-challenge -l app=customer --tail=100

# Ver eventos
kubectl get events -n tech-challenge --sort-by='.lastTimestamp'
```

---

## 📞 Ordem de Deploy Correta

Se começando do zero, siga esta ordem:

1. **tech-challenge-infra** (bootstrap + deploy)
   - Cria: VPC, EKS, ECR, Cognito
   
2. **tech-challenge-rds**
   - Cria: RDS PostgreSQL
   
3. **tech-challenge-dynamoDB**
   - Cria: DynamoDB tables

4. **tech-challenge-customer**
   - Deploy da aplicação customer

5. **tech-challenge-orders**
   - Deploy da aplicação orders

6. **tech-challenge-payments**
   - Deploy da aplicação payments

7. **tech-challenge-gateway**
   - Cria: API Gateway + Lambda Authorizer

---

## 🆘 Ainda com problemas?

1. Verifique a aba **Summary** do workflow
2. Procure por mensagens de erro específicas
3. Consulte os logs expandidos nas sections `<details>`
4. Execute com Debug Mode habilitado
5. Verifique se as credenciais AWS estão válidas

Se o problema persistir, abra uma Issue no repositório com:
- Link para o workflow run que falhou
- Mensagem de erro específica
- Passos que você já tentou
