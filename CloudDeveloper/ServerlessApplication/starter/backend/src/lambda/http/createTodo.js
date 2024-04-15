import middy from '@middy/core'
import cors from '@middy/http-cors'
import httpErrorHandler from '@middy/http-error-handler'
import { getUserId } from '../utils.mjs'
import { createTodo } from '../../businessLogic/todos.mjs'
import { createLogger } from '../../utils/logger.mjs'

const logger = createLogger('createTodoLambda')

export const handler = middy()
  .use(httpErrorHandler())
  .use(
    cors({
      credentials: true
    })
  )
  .handler(async (event) => {
    const todo = JSON.parse(event.body)
    const userId = getUserId(event)
    logger.info(
      `Lambda: Create todo with payload: ${todo} by user id: ${userId}`
    )
    const newTodo = await createTodo(todo, userId)
    return {
      statusCode: 201,
      body: JSON.stringify({
        item: newTodo
      })
    }
  })
