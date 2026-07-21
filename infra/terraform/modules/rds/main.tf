variable "project_name" { type = string }
variable "environment" { type = string }
variable "vpc_id" { type = string }
variable "private_subnets" { type = list(string) }
variable "db_instance_class" { type = string }
variable "allocated_storage" { type = number }

resource "aws_security_group" "rds" {
  name        = "${var.project_name}-rds-sg"
  description = "Security group for RDS"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-db-subnet-group"
  subnet_ids = var.private_subnets
}

resource "aws_db_instance" "main" {
  identifier           = "${var.project_name}-postgres"
  allocated_storage    = var.allocated_storage
  engine               = "postgres"
  engine_version       = "16"
  instance_class       = var.db_instance_class
  username             = "postgres"
  password             = "changeMeToSomethingSecure123!"
  db_subnet_group_name = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  skip_final_snapshot   = true
  multi_az              = true

  tags = {
    Name        = "${var.project_name}-rds"
    Environment = var.environment
  }
}

output "endpoint" { value = aws_db_instance.main.endpoint }
output "db_name" { value = aws_db_instance.main.db_name }
