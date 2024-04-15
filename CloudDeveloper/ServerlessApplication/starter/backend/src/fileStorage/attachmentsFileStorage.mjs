import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3'
import { getSignedUrl } from '@aws-sdk/s3-request-presigner'

const urlExpiration = parseInt(process.env.SIGNED_URL_EXPIRATION)
const s3Client = new S3Client()
const s3BucketName = process.env.ATTACHMENTS_S3_BUCKET

function generateAttachmentPresignedUrl(todoId) {
  return `https://${s3BucketName}.s3.amazonaws.com/${todoId}`
}

async function getUploadUrl(todoId) {
  const command = new PutObjectCommand({
    Bucket: s3BucketName,
    Key: todoId
  })
  const signedUrl = await getSignedUrl(s3Client, command, {
    expiresIn: urlExpiration
  })
  return signedUrl
}

export { generateAttachmentPresignedUrl, getUploadUrl }
