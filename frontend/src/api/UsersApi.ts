import { Role } from '../types/Role'
import { UserProfile } from '../types/UserProfile'
import { apiFetch } from './FetchWrapper'

export function fetchAllUsers(): Promise<UserProfile[]> {
    return apiFetch('/api/users')
}

export function fetchUserProfile(id: string): Promise<UserProfile> {
    return apiFetch('/api/users/profile/' + id)
}

export function fetchUsersByDepartmentId(id: number): Promise<UserProfile[]> {
    return apiFetch('/api/users/departments/' + id)
}
