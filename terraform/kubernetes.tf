# ============================================
# Kubernetes Deployment para Customer Service
# NOTA: O namespace é criado pelo repo tech-challenge-infra
# ============================================

# Kubernetes Deployment
resource "kubernetes_deployment" "app" {
  metadata {
    name      = var.app_name
    namespace = var.namespace
    labels = {
      app = var.app_name
    }
  }

  spec {
    replicas = var.replicas

    selector {
      match_labels = {
        app = var.app_name
      }
    }

    template {
      metadata {
        labels = {
          app = var.app_name
        }
      }

      spec {
        container {
          name  = var.app_name
          image = "${data.terraform_remote_state.infra.outputs.ecr_customer_url}:${var.image_tag}"

          port {
            container_port = var.container_port
          }

          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "250m"
              memory = "256Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/api/actuator/health"
              port = var.container_port
            }
            initial_delay_seconds = 60
            period_seconds        = 10
          }

          readiness_probe {
            http_get {
              path = "/api/actuator/health"
              port = var.container_port
            }
            initial_delay_seconds = 30
            period_seconds        = 5
          }
        }
      }
    }
  }

  # Ignora mudanças na imagem feitas por outros sistemas (ex: CI/CD manual)
  lifecycle {
    ignore_changes = [
      spec[0].template[0].spec[0].container[0].image
    ]
  }
}

# Kubernetes Service
resource "kubernetes_service" "app" {
  metadata {
    name      = var.app_name
    namespace = var.namespace
  }

  spec {
    selector = {
      app = var.app_name
    }

    port {
      port        = 80
      target_port = var.container_port
    }

    type = "ClusterIP"
  }
}
