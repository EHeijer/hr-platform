import { Role } from "./Role"

export type UserProfile = {
    id: string
    username: string
    name: string
    email: string
    permissions: string[]
    roles: Role[]
    departmentId: number
    image: string
    active: boolean
    phone: string
    address: string
    startDate: Date
}