'use client'
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface User {
    id: string
    email: string
    nickname: string
    profileImage?: string
    role: 'USER' | 'ADMIN'
}

interface AuthState {
    user: User | null
    token: string | null
    isAuthenticated: boolean
    login: (user: User, token: string) => void
    logout: () => void
    updateUser: (user: Partial<User>) => void
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set, get) => ({
            user: null,
            token: null,
            isAuthenticated: false,

            login: (user, token) => {
                set({
                    user,
                    token,
                    isAuthenticated: true,
                })
            },

            logout: () => {
                set({
                    user: null,
                    token: null,
                    isAuthenticated: false,
                })
            },

            updateUser: (updatedUser) => {
                const currentUser = get().user
                if (currentUser) {
                    set({
                        user: { ...currentUser, ...updatedUser },
                    })
                }
            },
        }),
        {
            name: 'auth-storage',
        }
    )
)