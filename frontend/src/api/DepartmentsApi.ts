import { Department } from '../types/Department'
import { UserProfile } from '../types/UserProfile'
import { apiFetch } from './FetchWrapper'

export function fetchAllDepartments(): Promise<Department[]> {
    return apiFetch('/api/departments')
}

export function fetchDepartmentById(id: number): Promise<Department> {
    return apiFetch('/api/departments/' + id)
}

export function updateDepartment(id: number, body: string): Promise<Department> {
    return apiFetch('/api/departments/' + id, "PUT", body)
}

export function createDepartment(body: string): Promise<Department> {
    return apiFetch('/api/departments', "POST", body)
}
