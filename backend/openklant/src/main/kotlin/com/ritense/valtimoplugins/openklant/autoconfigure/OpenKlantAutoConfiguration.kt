package com.ritense.valtimoplugins.openklant.autoconfigure

import com.ritense.plugin.service.PluginService
import com.ritense.processdocument.service.ProcessDocumentService
import com.ritense.valtimoplugins.openklant.client.OpenKlantClient
import com.ritense.valtimoplugins.openklant.plugin.OpenKlantPluginFactory
import com.ritense.valtimoplugins.openklant.resolver.OpenKlantValueResolverFactory
import com.ritense.valtimoplugins.openklant.service.DefaultOpenKlantService
import com.ritense.valtimoplugins.openklant.service.OpenKlantService
import com.ritense.valtimoplugins.openklant.service.PartijFactory
import com.ritense.valtimoplugins.openklant.util.ReflectionUtil
import com.ritense.zakenapi.service.ZaakDocumentService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties
class OpenKlantAutoConfiguration {
    @Bean
    fun openKlantPluginClient(
        openKlantWebClientBuilder: WebClient.Builder,
        env: Environment,
    ): OpenKlantClient = OpenKlantClient(openKlantWebClientBuilder, isDevProfile = env.acceptsProfiles(Profiles.of("dev")))

    @Bean
    fun partijFactory(): PartijFactory = PartijFactory()

    @Bean
    fun openKlantService(
        openKlantClient: OpenKlantClient,
        partijFactory: PartijFactory,
    ): OpenKlantService =
        DefaultOpenKlantService(
            openKlantClient,
            partijFactory,
        )

    @Bean
    fun openKlantPluginFactory(
        pluginService: PluginService,
        openKlantService: OpenKlantService,
        reflectionUtil: ReflectionUtil,
    ): OpenKlantPluginFactory =
        OpenKlantPluginFactory(
            pluginService,
            openKlantService,
            reflectionUtil,
        )

    @Bean
    fun openKlantValueResolverFactory(
        processDocumentService: ProcessDocumentService,
        zaakDocumentService: ZaakDocumentService,
        openKlantService: OpenKlantService,
        reflectionUtil: ReflectionUtil,
    ) = OpenKlantValueResolverFactory(
        processDocumentService,
        zaakDocumentService,
        openKlantService,
        reflectionUtil,
    )

    @Bean
    fun reflectionUtil() = ReflectionUtil()
}