import { CurrentUser } from '../types/CurrentUser'
import { apiFetch } from './FetchWrapper'

export function fetchCurrentUser(): Promise<CurrentUser> {
    return apiFetch('/api/auth/current-user')
}