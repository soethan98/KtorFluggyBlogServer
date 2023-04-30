import com.example.domain.model.Endpoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.rootRoute() {
    get(Endpoint.Root.path) {
        call.respondText("Welcome to Ktor Server!")

    }
}