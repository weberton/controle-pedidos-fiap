variable "resource_group_name" {
  description = "Resource group for FIAP-Architecture - SOAT33"
  type        = string
  default     = "controlepedidos-rg"
}

variable "location" {
  description = "East US"
  type        = string
  default     = "East US"
}

variable "vnet_name" {
  description = "Vnet for FIAP-Architecture - SOAT33"
  type        = string
  default     = "controlepedidos-vnet"
}

variable "vnet_cidr" {
  description = "CIDR da VNet"
  type        = string
  default     = "10.0.0.0/16"
}

variable "subnet_cidr" {
  description = "CIDR da Subnet pública"
  type        = string
  default     = "10.0.1.0/24"
}

variable "aks_cluster_name" {
  description = "Nome do cluster AKS"
  type        = string
  default     = "controlepedidos-aks"
}

variable "aks_node_count" {
  description = "Número de nós no AKS"
  type        = number
  default     = 1
}

variable "aks_node_vm_size" {
  description = "Tamanho da VM dos nós do AKS"
  type        = string
  default     = "Standard_DS2_v2"
}