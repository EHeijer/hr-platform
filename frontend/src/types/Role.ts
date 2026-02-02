import { UserProfile } from "./UserProfile"

export type Role = {
  id?: number
  name: string
  description: string
  users?: UserProfile[]
}