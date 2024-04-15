# Project Steps

## 1. Docker Image Setup

### Set up Docker Repository on AWS ECR
- Create a Docker repository on Amazon ECR to store Docker images.

## 2. Continuous Integration Pipeline

### Create CodeBuild Project
- Implement a CodeBuild project for continuous integration.
- Configure the pipeline to build and push Docker images to ECR based on trigger events from the GitHub repository.

## 3. Kubernetes Service Setup

### Create EKS Cluster and Worker Node Group
- Establish an Amazon EKS cluster.
- Configure a worker node group with necessary IAM roles and suitable hardware configurations.

### Establish Connection
- Set up the connection between the local development environment and the AWS EKS service.

## 4. Database Configuration

### Install and Manage PostgreSQL with Helm Charts
- Utilize Helm Charts to install and manage PostgreSQL.

### Run Seed Files
- Execute Seed Files located in the 'db' folder to create tables and populate data in the PostgreSQL database.

## 5. Deploy Configuration

### Create Configuration Files
- Generate configuration files including `secret.yml`, `configmap.yml`, `deployment.yml`, and `service.yml`.

### Deploy Microservice
- Use `kubectl apply -f` with the generated configuration files to deploy the microservice as a pod in Kubernetes.

## 6. Monitoring Application

### Install CloudWatch
- Execute `cloudwatch-install.sh` to install CloudWatch for monitoring the application.

### Verify Application and Database
- Ensure the database service and the application are running as expected.

These steps provide a comprehensive guide to setting up, deploying, and monitoring the microservice using Docker, AWS ECR, Amazon EKS, PostgreSQL, and CloudWatch.