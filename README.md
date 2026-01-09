# Tech Challenge - Customer

Repositório responsável pelo microserviço de clientes.

## O que este repositório faz

- **API de Clientes** - CRUD de clientes
- **Autenticação** - Integração com AWS Cognito
- **Deployment K8s** - Deploy no EKS via Terraform

## Dependências

| Dependência | Descrição |
|-------------|-----------|
| tech-challenge-infra | EKS Cluster e ECR (via remote state) |
| Terraform >= 1.10.0 | Ferramenta de IaC |
| Java 17 | Runtime da aplicação |
| Maven | Build da aplicação |

## Secrets Necessários (GitHub)

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_SESSION_TOKEN` (obrigatório para AWS Academy Learner Lab)
