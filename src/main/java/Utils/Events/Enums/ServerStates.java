package Utils.Events.Enums;

/**
 * Enumerates the possible states of a server within the system, reflecting key
 * stages in the server's lifecycle from startup to shutdown. This enumeration
 * helps manage and monitor the server's status, facilitating responsive control
 * and maintenance operations.
 *
 * <p>
 * The {@code ServerStates} enum provides a clear indication of the server's
 * current operational state, supporting system health checks, maintenance
 * operations, and dynamic updates. By tracking these states, the system can
 * execute appropriate actions such as initiating startup procedures, handling
 * client connections during normal operation, safely closing down, and applying
 * updates.
 * </p>
 *
 * <p>
 * States:
 * </p>
 *
 * <ul>
 * <li>
 * {@code STARTING} - Indicates the server is in the process of starting up.
 * This state covers the initialization of system resources, loading of
 * configuration settings, and any other preparatory tasks required to bring the
 * server to an operational state.
 * </li>
 *
 * <li>
 * {@code RUNNING} - Signifies that the server is operational and capable of
 * handling client requests. In this state, the server is actively serving
 * clients, performing its primary functions.
 * </li>
 *
 * <li>
 * {@code CLOSING} - Represents the state where the server is in the process
 * of shutting down. This state allows for graceful termination, ensuring that
 * ongoing processes are completed and resources are released properly before
 * the server shuts down.
 * </li>
 *
 * <li>
 * {@code UPDATE} - Denotes a special state where the server is undergoing
 * updates. This could involve applying software patches, upgrading system
 * components, or any other maintenance activities that require the server to
 * temporarily suspend normal operations. The server may either automatically
 * enter a maintenance mode or shut down completely, depending on the nature of
 * the updates.
 * </li>
 * </ul>
 *
 * <p>
 * Monitoring and managing these server states can significantly enhance the
 * reliability and efficiency of the server's operation within the system,
 * allowing for proactive maintenance and minimized downtime.
 * </p>
 */
public enum ServerStates {
    /**
     * The server is in the process of starting up, initializing necessary resources
     * and configurations.
     */
    STARTING,

    /**
     * The server is fully operational and ready to handle client requests.
     */
    RUNNING,

    /**
     * The server is in the process of shutting down, ensuring all processes are
     * gracefully completed.
     */
    CLOSED,

    /**
     * The server is undergoing updates, which may require temporary suspension of
     * normal operations.
     */
    UPDATE,
}
