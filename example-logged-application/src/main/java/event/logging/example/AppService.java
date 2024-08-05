package event.logging.example;

import event.logging.AuthenticateAction;
import event.logging.AuthenticateEventAction;
import event.logging.AuthenticateLogonType;
import event.logging.Banner;
import event.logging.ComplexLoggedOutcome;
import event.logging.EventLoggingService;
import event.logging.MultiObject;
import event.logging.OtherObject;
import event.logging.Outcome;
import event.logging.Query;
import event.logging.SearchEventAction;
import event.logging.SimpleQuery;
import event.logging.User;
import event.logging.ViewEventAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    private static final String BANNER = "" +
            "with great power comes great responsibility.\n" +
            "Do you accept this responsibility? (y/n):";
    private final EventLoggingService eventLoggingService;
    private final UserContext userContext;

    public AppService(final EventLoggingService eventLoggingService, final UserContext userContext) {
        this.eventLoggingService = eventLoggingService;
        this.userContext = userContext;
    }

    void showShutdownBanner() {
        // Use the log method when you want to manually deal with exceptions or the logged event outcome
        // is always success.
        LOGGER.info("The system is about to be shutdown for maintenance, log off now!");
        eventLoggingService.log(
                "LogoffNowBanner",
                "User shown logoff now banner",
                ViewEventAction.builder()
                        .addBanner(Banner.builder()
                                .withMessage("The system is about to be shutdown for maintenance, log off now!")
                                .build())
                        .build());
    }

    void loginUser() {

        final Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter your name:");

        final String userId = scanner.nextLine();

        final boolean isLoggedIn = eventLoggingService.loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + userId + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withId(userId)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .withLogonType(AuthenticateLogonType.INTERACTIVE)
                        .build())
                .withComplexLoggedResult(eventAction ->
                        performLogin(userId, eventAction))
                .getResultAndLog();

        if (!isLoggedIn) {
            LOGGER.error("Invalid user ID, quiting!");
            System.exit(1);
        }
    }

    private ComplexLoggedOutcome<Boolean, AuthenticateEventAction> performLogin(
            final String userId,
            final AuthenticateEventAction eventAction) {

        // Perform login
        if (userId == null || userId.isEmpty()) {
            return ComplexLoggedOutcome.failure(
                    false,
                    eventAction,
                    "Invalid username");
        } else {
            userContext.setUserId(userId);
            return ComplexLoggedOutcome.success(
                    true,
                    eventAction);
        }
    }

    void logoffUser() {

        eventLoggingService.loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + userContext.getUserId() + " logged out")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withId(userContext.getUserId())
                                .build())
                        .withAction(AuthenticateAction.LOGOFF)
                        .build())
                .withSimpleLoggedAction(() -> {
                    // Perform logoff
                    LOGGER.info("Logging off user {}", userContext.getUserId());
                    userContext.setUserId(null);
                })
                .runActionAndLog();
    }

    boolean showConfirmationBanner() {
        return eventLoggingService.loggedWorkBuilder()
                .withTypeId("ShowBanner")
                .withDescription("User shown acceptable use banner")
                .withDefaultEventAction(ViewEventAction.builder()
                        .addBanner(Banner.builder()
                                .withMessage("With great power comes great responsibility." +
                                        "Do you accept this responsibility?")
                                .build())
                        .build())
                .withSimpleLoggedResult(() -> {
                    LOGGER.info("Show banner and get confirmation");

                    final Scanner scanner = new Scanner(System.in);
                    String answer;
                    do {
                        System.out.println("\n" + userContext.getUserId() + ", " + BANNER);
                        answer = scanner.next().toLowerCase();
                    } while (!answer.matches("[yn]"));

                    return answer.equals("y");
                })
                .getResultAndLog();
    }

    List<String> performSearch() {

        final List<String> results = eventLoggingService.loggedWorkBuilder()
                .withTypeId("listMethods private")
                .withDescription("List all private method names")
                .withDefaultEventAction(SearchEventAction.builder()
                        .withQuery(Query.builder()
                                .withSimple(SimpleQuery.builder()
                                        .withInclude("private")
                                        .withExclude("lambda")
                                        .build())
                                .build())
                        .build())
                .withComplexLoggedResult(eventAction -> {

                    // Do the work
                    final List<String> privateMethods = Arrays.stream(this.getClass().getDeclaredMethods())
                            .filter(method -> Modifier.isPrivate(method.getModifiers()))
                            .map(Method::getName)
                            .filter(name -> !name.startsWith("lambda"))
                            .collect(Collectors.toList());

                    // Create a new SearchEventAction that is a copy of the one we created
                    // but with the results of the search added.
                    final SearchEventAction newEventAction = eventAction.newCopyBuilder()
                            .withResults(MultiObject.builder()
                                    .addObjects(privateMethods.stream()
                                            .map(name -> OtherObject.builder()
                                                    .withName(name)
                                                    .withType("Method")
                                                    .build())
                                            .collect(Collectors.toList()))
                                    .build())
                            .withTotalResults(BigInteger.valueOf(privateMethods.size()))
                            .build();

                    // Return the success outcome
                    return ComplexLoggedOutcome.success(privateMethods, newEventAction);
                })
                .withCustomExceptionHandler((eventAction, throwable) -> {
                    // Provide a modified SearchEventAction based on the exception.
                    // If you don't provide a handler then by default the success and description
                    // are set for you.
                    return eventAction.newCopyBuilder()
                            .withTotalResults(BigInteger.valueOf(0))
                            .withOutcome(Outcome.builder()
                                    .withSuccess(false)
                                    .withDescription(throwable.getMessage())
                                    .build())
                            .build();
                })
                .getResultAndLog();

        LOGGER.info("Search returned :\n{}", String.join("\n", results));

        return results;
    }
}
