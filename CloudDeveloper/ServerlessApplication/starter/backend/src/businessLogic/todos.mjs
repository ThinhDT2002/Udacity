import * as uuid from 'uuid'

import { TodosAccess } from '../dataLayer/todosAccess.mjs'
import {
  getUploadUrl,
  generateAttachmentPresignedUrl
} from '../fileStorage/attachmentsFileStorage.mjs'
import { createLogger } from '../utils/logger.mjs'

const logger = createLogger('todosBussinessLogic')
const todosAccess = new TodosAccess()

async function getTodosByUserId(userId) {
  logger.info(`Business logic: Get todo by user id: ${userId}`)
  return todosAccess.getTodos(userId)
}

async function createTodo(newTodo, userId) {
  logger.info(
    `Business logic: Create todo with payload: ${newTodo} by user id: ${userId}`
  )

  const todoId = uuid.v4()
  const createdAt = new Date().toISOString()
  const attachmentUrl = generateAttachmentPresignedUrl(todoId)

  return await todosAccess.createTodo({
    userId,
    todoId,
    createdAt,
    done: false,
    attachmentUrl: attachmentUrl,
    ...newTodo
  })
}

async function updateTodo(todoId, userId, updatedTodo) {
  logger.info(
    `Business logic: Processing update todo with payload: ${updatedTodo} by user id: ${userId} and todo id: ${todoId}`
  )
  return todosAccess.updateTodo(todoId, userId, updatedTodo)
}

async function deleteTodo(todoId, userId) {
  logger.info(
    `Business logic: Delete todo with user id: ${userId} and todo id: ${todoId}`
  )
  return todosAccess.deleteTodo(todoId, userId)
}

async function createAttachmentPresignedUrl(todoId) {
  logger.info(
    `Business logic: Create attachment url with todo id: ${todoId}`
  )
  return await getUploadUrl(todoId)
}

export {
  getTodosByUserId,
  createTodo,
  updateTodo,
  deleteTodo,
  createAttachmentPresignedUrl
}
