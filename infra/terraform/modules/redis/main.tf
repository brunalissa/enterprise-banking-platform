variable "project_name" { type = string }
variable "environment" { type = string }
variable "vpc_id" { type = string }
variable "subnet_ids" { type = list(string) }

resource "aws_security_group" "redis" {
  name        = "${var.project_name}-redis-sg"
  description = "Security group for Redis"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }
}

resource "aws_elasticache_subnet_group" "main" {
  name       = "${var.project_name}-redis-subnet"
  subnet_ids = var.subnet_ids
}

resource "aws_elasticache_replication_group" "main" {
  replication_group_id = "${var.project_name}-redis"
  replication_group_description = "Redis for ${var.project_name}"
  node_type            = "cache.t3.micro"
  number_cache_clusters = 2
  subnet_group_name    = aws_elasticache_subnet_group.main.name
  security_group_ids   = [aws_security_group.redis.id]
  engine_version       = "7.0"
  multi_az_enabled     = true

  tags = {
    Name        = "${var.project_name}-redis"
    Environment = var.environment
  }
}

output "endpoint" {
  value = aws_elasticache_replication_group.main.primary_endpoint_address
}
