import middy from '@middy/core'
import cors from '@middy/http-cors'
import httpErrorHandler from '@middy/http-error-handler'
import { updateTodo } from '../../businessLogic/todos.mjs'
import { getUserId } from '../utils.mjs'
import { createLogger } from '../../utils/logger.mjs'

const logger = createLogger('updateTodoLambda')

export const handler = middy()
  .use(httpErrorHandler())
  .use(
    cors({
      credentials: true
    })
  )
  .handler(async (event) => {
    const todoId = event.pathParameters.todoId
    const updatedTodo = JSON.parse(event.body)
    const userId = getUserId(event)
    logger.info(
      `Lambda: Update todo with payload: ${updatedTodo} by user id: ${userId} and todo id: ${todoId}`
    )

    await updateTodo(todoId, userId, updatedTodo)
    const responseBody = '';
    return {
      statusCode: 200,
      body: responseBody
    }
  })
