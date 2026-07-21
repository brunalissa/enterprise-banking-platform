variable "project_name" { type = string }
variable "environment" { type = string }
variable "vpc_id" { type = string }
variable "subnet_ids" { type = list(string) }

resource "aws_security_group" "msk" {
  name        = "${var.project_name}-msk-sg"
  description = "Security group for MSK"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 9092
    to_port     = 9094
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }
}

resource "aws_msk_cluster" "main" {
  cluster_name           = "${var.project_name}-kafka"
  kafka_version          = "3.5.1"
  number_of_broker_nodes = 2

  broker_node_group_info {
    instance_type   = "kafka.t3.small"
    client_subnets  = var.subnet_ids
    security_groups = [aws_security_group.msk.id]
    storage_info {
      ebs_storage_info {
        volume_size = 50
      }
    }
  }

  tags = {
    Name        = "${var.project_name}-msk"
    Environment = var.environment
  }
}

output "bootstrap_brokers" {
  value = aws_msk_cluster.main.bootstrap_brokers
}
