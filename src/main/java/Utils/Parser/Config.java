package Utils.Parser;

import java.nio.file.Paths;

/**
 * Represents configuration settings for the system, encapsulating various
 * parameters such as server amount, task pool size, and dimensions in which are
 * going to be used to divide the image. This class provides a structured
 * approach to accessing and modifying configuration settings, which are crucial
 * for the system's operation and performance tuning.
 *
 * <p>
 * Instances of this class are typically populated from configuration files or
 * other external data sources at startup, providing a centralized point of
 * access for configuration parameters throughout the system.
 * </p>
 */
public class Config {
    private int serverAmount;
    private int taskPoolSize;
    private int columns;
    private int rows;
    private final String savePath = Paths.get("src","results").toString();
    /**
     * Gets the configured amount of servers to be used by the system.
     *
     * @return The amount of servers configured.
     */
    public int getServerAmount() {
        return serverAmount;
    }

    /**
     * Sets the amount of servers that the system should use.
     *
     * @param serverAmount The amount of servers.
     */
    public void setServerAmount(int serverAmount) {
        this.serverAmount = serverAmount;
    }

    /**
     * Gets the size of the task pool, which determines the number of tasks
     * that can be processed concurrently.
     *
     * @return The task pool size.
     */
    public int getTaskPoolSize() {
        return taskPoolSize;
    }

    /**
     * Sets the amount of executors for the task pool for concurrent task
     * processing.
     *
     * @param taskPoolSize The size of the task pool.
     */
    public void setTaskPoolSize(int taskPoolSize) {
        this.taskPoolSize = taskPoolSize;
    }

    /**
     * Gets the number of columns the image is going to be divided.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Sets the number of columns the image is going to be divided.
     *
     * @param columns The number of columns.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Gets the number of rows the image is going to be divided.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows number of rows the image is going to be divided.
     *
     * @param rows The number of rows.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Gets the save path that image is going to be saved.
     *
     * @return The save path;
     */
    public String getSavePath() {
        return savePath;
    }
}
