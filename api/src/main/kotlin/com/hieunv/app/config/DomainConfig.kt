package com.hieunv.app.config

import com.hieunv.app.core.auth.UserRepository
import com.hieunv.app.core.auth.UserUseCase
import com.hieunv.app.core.auth.UserUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainConfig {

    @Bean
    fun userUseCase(userRepository: UserRepository): UserUseCase {
        return UserUseCaseImpl(userRepository)
    }
}
