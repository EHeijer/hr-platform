import { Role } from '../types/Role'
import { apiFetch } from './FetchWrapper'

export function fetchAllRoles(): Promise<Role[]> {
    return apiFetch('/api/users/roles')
}

export function updateRole(id: number, body: string): Promise<Role> {
    return apiFetch('/api/users/roles/' + id, "PUT", body)
}

export function createRole(body: string): Promise<Role> {
    return apiFetch('/api/users/roles', "POST", body)
}