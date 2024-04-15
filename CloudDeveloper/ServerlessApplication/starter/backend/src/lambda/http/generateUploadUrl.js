import middy from '@middy/core'
import cors from '@middy/http-cors'
import httpErrorHandler from '@middy/http-error-handler'
import { createAttachmentPresignedUrl } from '../../businessLogic/todos.mjs'
import { createLogger } from '../../utils/logger.mjs'

const logger = createLogger('generateUploadUrlLambda')

export const handler = middy()
  .use(httpErrorHandler())
  .use(
    cors({
      credentials: true
    })
  )
  .handler(async (event) => {
    const todoId = event.pathParameters.todoId
    logger.info(
      `Lambda: Create attachment url with todo id: ${todoId}`
    )

    const attachmentUrl = await createAttachmentPresignedUrl(todoId)
    return {
      statusCode: 201,
      body: JSON.stringify({
        uploadUrl: attachmentUrl
      })
    }
  })
