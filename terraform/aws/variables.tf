variable "region" {
  default = "us-east-1"
}

variable "cluster_name" {
  default = "controlepedidos-cluster"
}

variable "node_instance_type" {
  default = "t3.small"
}

variable "eks_admin_principal_arn" {
  type        = string
  description = "ARN do usuário terá acesso ao cluster EKS"
  default = "arn:aws:iam::967246349191:root" #replace it
}