terraform {
  required_version = ">= 1.5"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket = "enterprise-banking-platform-tfstate"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = var.aws_region
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "environment" {
  type    = string
  default = "production"
}

variable "project_name" {
  type    = string
  default = "enterprise-banking-platform"
}

# ─── VPC Module ───
module "vpc" {
  source = "./modules/vpc"

  project_name = var.project_name
  environment  = var.environment
  cidr_block   = "10.0.0.0/16"
}

# ─── EKS Module ───
module "eks" {
  source = "./modules/eks"

  project_name = var.project_name
  environment  = var.environment
  vpc_id       = module.vpc.vpc_id
  subnet_ids   = module.vpc.private_subnet_ids
}

# ─── RDS Module ───
module "rds" {
  source = "./modules/rds"

  project_name     = var.project_name
  environment      = var.environment
  vpc_id           = module.vpc.vpc_id
  private_subnets  = module.vpc.private_subnet_ids
  db_instance_class = "db.t3.medium"
  allocated_storage = 100
}

# ─── Redis Module ───
module "redis" {
  source = "./modules/redis"

  project_name  = var.project_name
  environment   = var.environment
  vpc_id        = module.vpc.vpc_id
  subnet_ids    = module.vpc.private_subnet_ids
}

# ─── MSK (Kafka) Module ───
module "msk" {
  source = "./modules/msk"

  project_name = var.project_name
  environment  = var.environment
  vpc_id       = module.vpc.vpc_id
  subnet_ids   = module.vpc.private_subnet_ids
}

# Outputs
output "vpc_id" {
  value = module.vpc.vpc_id
}

output "eks_cluster_name" {
  value = module.eks.cluster_name
}

output "rds_endpoint" {
  value = module.rds.endpoint
}

output "redis_endpoint" {
  value = module.redis.endpoint
}

output "msk_bootstrap_brokers" {
  value = module.msk.bootstrap_brokers
}
