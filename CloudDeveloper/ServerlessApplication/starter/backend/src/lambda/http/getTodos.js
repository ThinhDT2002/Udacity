import middy from '@middy/core'
import cors from '@middy/http-cors'
import httpErrorHandler from '@middy/http-error-handler'
import { getTodosByUserId } from '../../businessLogic/todos.mjs'
import { getUserId } from '../utils.mjs'
import { createLogger } from '../../utils/logger.mjs'

const logger = createLogger('getTodosLambda')

export const handler = middy()
  .use(httpErrorHandler())
  .use(
    cors({
      credentials: true
    })
  )
  .handler(async (event) => {
    const userId = getUserId(event)
    logger.info(`Lambda: Get todo by user id: ${userId}`)
    const todos = await getTodosByUserId(userId)
    return {
      statusCode: 200,
      body: JSON.stringify({
        items: todos
      })
    }
  })
