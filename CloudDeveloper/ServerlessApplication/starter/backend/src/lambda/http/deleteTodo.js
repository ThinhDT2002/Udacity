import middy from '@middy/core'
import cors from '@middy/http-cors'
import httpErrorHandler from '@middy/http-error-handler'
import { deleteTodo } from '../../businessLogic/todos.mjs'
import { getUserId } from '../utils.mjs'
import { createLogger } from '../../utils/logger.mjs'

const logger = createLogger('deleteTodoLambda')

export const handler = middy()
  .use(httpErrorHandler())
  .use(
    cors({
      credentials: true
    })
  )
  .handler(async (event) => {
    const todoId = event.pathParameters.todoId
    const userId = getUserId(event)
    logger.info(
      `Lambda: Delete todo with user id: ${userId} and todo id: ${todoId}`
    )
    await deleteTodo(todoId, userId)
    const responseBody = '';
    return {
      statusCode: 200,
      body: responseBody
    }
  })
