provider "aws"{
    region = "ap-southeast-1"
}

resource "aws_vpc" "vpc" {
    cidr_block = "172.50.0.0/16"
    enable_dns_hostnames = true
    tags = {
        Name = "gcashMini-g1"
    }
  
}

resource "aws_internet_gateway" "igw" {
    vpc_id = aws_vpc.vpc.id
    tags = {
      Name = "GcashMini-igw"
    }
}

resource "aws_subnet" "public_subnet" {
   vpc_id = aws_vpc.vpc.id 
   cidr_block = "172.50.1.0/24"
   map_public_ip_on_launch = true
   availability_zone = "ap-southeast-1a"
   tags = {
     "Name" = "public-subnet-gcashMini-g1"
   }
}

resource "aws_subnet" "private_subnet" {
   vpc_id = aws_vpc.vpc.id 
   cidr_block = "172.50.2.0/24"
   availability_zone = "ap-southeast-1a"
   tags = {
     "Name" = "private-subnet-gcashMini-g1"
   }
}

resource "aws_route_table" "public-rt" {
    vpc_id = aws_vpc.vpc.id
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.igw.id
    }
    tags ={
        Name = "public-rt-gcashMini-g1"
    }
  
}

resource "aws_route_table_association" "public-rt-assoc"{
    subnet_id = aws_subnet.public_subnet.id
    route_table_id = aws_route_table.public-rt.id
}

resource "aws_key_pair" "keypair" {
  key_name = "gcashMiniG1"
  public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDbmtFAVP5LYqgTGwwxsQl0surYlWIlJvWR8SG/E6p73T9Z6LJuJ0esIGLDB0DL8L2azFRrEJCo1GpM4oHkTa2QJG2bKHYfjx+D+kfDhv8Mn2wS/JbsBau4wcpo/kVU5atqGCRwTjfR9yCYU2GsmtTgJtS6jAI1AiLQMNDjXNt+P++6tQL3Kxp/RQYWyZP7UqStxB+CjaRmFY0nweyJIBGctZbsV2+nNHOuLBv3A7N5G0xW4gNBodYJM/vMTCERevup0HffEOeImEQ9aNmLatzUNhMEP1lZylNFP41LIeHikKzqNHSYXnFCJR7NR8LiQJ/ISWZaeoBR5CaXo9o17d0dqoCEUwHVcgXw5YbrsP42fkjVEgDZq0lYldT2dHcozJXQbHaMQ508PiUSAkWbMt3uf9IKBkjs1cNwooBwfDH+jMSogp0HRzdzmMArxr/hlxXn1vA5inAz0ZkOM9d98mjXjU9DgPorLY0PWlwmgp32RXfukJckhsXgM4UE1WFkmS8= johnashly@johnashly-mynt"
}

resource "aws_security_group" "apiGatewaySG"{
  name = "G1-apigwSG"
  vpc_id = aws_vpc.vpc.id
  ingress {
    from_port = 22
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8080
    protocol = "tcp"
    to_port = 8080
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    protocol = "-1"
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "accountSG"{
  name = "G1-accountSG"
  vpc_id = aws_vpc.vpc.id
  ingress {
    from_port = 22
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8081
    protocol = "tcp"
    to_port = 8081
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    protocol = "-1"
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_security_group" "activitySG"{
  name = "G1-activitySG"
  vpc_id = aws_vpc.vpc.id
  ingress {
    from_port = 22
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8082
    protocol = "tcp"
    to_port = 8082
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    protocol = "-1"
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "productSG"{
  name = "G1-productsSG"
  vpc_id = aws_vpc.vpc.id
  ingress {
    from_port = 22
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8083
    protocol = "tcp"
    to_port = 8083
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    protocol = "-1"
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "purchaseSG"{
  name = "G1-purchaseSG"
  vpc_id = aws_vpc.vpc.id
  ingress {
    from_port = 22
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8084
    protocol = "tcp"
    to_port = 8084
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    protocol = "-1"
    to_port = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "apigw" {
  ami = "ami-03ca998611da0fe12"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public_subnet.id
  key_name = aws_key_pair.keypair.key_name
  vpc_security_group_ids = [aws_security_group.apiGatewaySG.id]
  # user_data = file("init_gateway.sh")
  user_data =<<EOF
  #!/bin/bash
yum update -y
aws s3 cp s3://g1-microservices/api-gateway/api-gateway-0.0.1-SNAPSHOT.jar /home/ec2-user/api-gateway-0.0.1-SNAPSHOT.jar
aws s3 cp s3://g1-microservices/api-gateway/boot-ctl.sh /home/ec2-user/boot-ctl.sh
aws s3 cp s3://g1-microservices/api-gateway/application.properties /home/ec2-user/application.properties
amazon-linux-extras install java-openjdk11 -y
sed -i 's/localhost:8081/${aws_instance.account.public_ip}:8081/g' /home/ec2-user/application.properties
sed -i 's/localhost:8082/${aws_instance.activity.public_ip}:8082/' /home/ec2-user/application.properties
sed -i 's/localhost:8083/${aws_instance.products.public_ip}:8083/' /home/ec2-user/application.properties
sed -i 's/localhost:8084/${aws_instance.purchase.public_ip}:8084/' /home/ec2-user/application.properties
chmod +x /home/ec2-user/boot-ctl.sh
./boot-ctl.sh start
EOF
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  tags = {
    Name = "G1-GatewayService"
  }
}

resource "aws_instance" "account" {
  ami = "ami-03ca998611da0fe12"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public_subnet.id
  key_name = aws_key_pair.keypair.key_name
  vpc_security_group_ids = [aws_security_group.accountSG.id]
  associate_public_ip_address = true
  # user_data = file("init_account.sh")
  user_data = <<EOF
#!/bin/bash
yum update -y
aws s3 cp s3://g1-microservices/account-management/account-management-0.0.1-SNAPSHOT.jar /home/ec2-user/account-management-0.0.1-SNAPSHOT.jar
aws s3 cp s3://g1-microservices/account-management/boot-ctl.sh /home/ec2-user/boot-ctl.sh
aws s3 cp s3://g1-microservices/account-management/application.properties /home/ec2-user/application.properties
amazon-linux-extras install java-openjdk11 -y
sed -i 's/localhost:8082/${aws_instance.activity.public_ip}:8082/' /home/ec2-user/application.properties
chmod +x /home/ec2-user/boot-ctl.sh
./boot-ctl.sh start
EOF
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  tags = {
    Name = "G1-AccountService"
  }
}

resource "aws_instance" "activity" {
  ami = "ami-03ca998611da0fe12"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public_subnet.id
  key_name = aws_key_pair.keypair.key_name
  vpc_security_group_ids = [aws_security_group.activitySG.id]
  associate_public_ip_address = true
//  user_data = file("init_activity.sh")
  user_data = <<EOF
#!/bin/bash
yum update -y
aws s3 cp s3://g1-microservices/activity/activity-0.0.1-SNAPSHOT.jar /home/ec2-user/activity-0.0.1-SNAPSHOT.jar
aws s3 cp s3://g1-microservices/activity/boot-ctl.sh /home/ec2-user/boot-ctl.sh
aws s3 cp s3://g1-microservices/activity/application.properties /home/ec2-user/application.properties
amazon-linux-extras install java-openjdk11 -y
chmod +x /home/ec2-user/boot-ctl.sh
./boot-ctl.sh start
EOF
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  tags = {
    Name = "G1-ActivityService"
  }
}

resource "aws_instance" "purchase" {
  ami = "ami-03ca998611da0fe12"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public_subnet.id
  key_name = aws_key_pair.keypair.key_name
  vpc_security_group_ids = [aws_security_group.purchaseSG.id]
  associate_public_ip_address = true
  # user_data = file("init_purchase.sh")
  user_data =<<EOF
  #!/bin/bash
yum update -y
aws s3 cp s3://g1-microservices/purchase/purchase-0.0.1-SNAPSHOT.jar /home/ec2-user/purchase-0.0.1-SNAPSHOT.jar
aws s3 cp s3://g1-microservices/purchase/boot-ctl.sh /home/ec2-user/boot-ctl.sh
aws s3 cp s3://g1-microservices/purchase/application.properties /home/ec2-user/application.properties
amazon-linux-extras install java-openjdk11 -y
sed -i 's/localhost:8081/${aws_instance.account.public_ip}:8081/' /home/ec2-user/application.properties
sed -i 's/localhost:8082/${aws_instance.activity.public_ip}:8082/' /home/ec2-user/application.properties
sed -i 's/localhost:8083/${aws_instance.products.public_ip}:8083/' /home/ec2-user/application.properties
chmod +x /home/ec2-user/boot-ctl.sh
./boot-ctl.sh start
EOF
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  tags = {
    Name = "G1-PurchaseService"
  }
}
//
resource "aws_instance" "products" {
  ami = "ami-03ca998611da0fe12"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public_subnet.id
  key_name = aws_key_pair.keypair.key_name
  vpc_security_group_ids = [aws_security_group.productSG.id]
  associate_public_ip_address = true
  # user_data = file("init_product.sh")
  user_data =<<EOF
#!/bin/bash
yum update -y
aws s3 cp s3://g1-microservices/product/product-0.0.1-SNAPSHOT.jar /home/ec2-user/product-0.0.1-SNAPSHOT.jar
aws s3 cp s3://g1-microservices/product/boot-ctl.sh /home/ec2-user/boot-ctl.sh
aws s3 cp s3://g1-microservices/product/application.properties /home/ec2-user/application.properties
amazon-linux-extras install java-openjdk11 -y
sed -i 's/localhost:8082/${aws_instance.activity.public_ip}:8082/' /home/ec2-user/application.properties
chmod +x /home/ec2-user/boot-ctl.sh
./boot-ctl.sh start
  EOF
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  tags = {
    Name = "G1-ProductService"
  }
}

resource "aws_iam_role" "s3_role" {
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "ec2.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
      }
    ]
}
EOF
}

resource "aws_iam_instance_profile" "instance_profile" {
  name = "G1-S3instanceProfile"
  role = aws_iam_role.s3_role.name
}
resource "aws_iam_role_policy" "s3_policy" {
  name = "g1-s3_policy"
  role = aws_iam_role.s3_role.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "s3:GetObject"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::g1-microservices/*"
    }
  ]
}
EOF
}
output "apiGWEC2" {
  value = aws_instance.apigw.public_ip
}

output "accountEC2" {
  value = aws_instance.account.public_ip
}
output "activityEC2" {
  value = aws_instance.activity.public_ip
}
output "productEC2" {
  value = aws_instance.products.public_ip
}
output "purchaseEC2" {
  value = aws_instance.purchase.public_ip
}

