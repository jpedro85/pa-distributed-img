package Utils.Parser;

public class Config {
    private int serverAmount;
    private int taskPoolSize;
    private int columns;
    private int rows;

    public int getServerAmount() {
        return serverAmount;
    }

    public void setServerAmount(int serverAmount) {
        this.serverAmount = serverAmount;
    }

    public int getTaskPoolSize() {
        return taskPoolSize;
    }

    public void setTaskPoolSize(int taskPoolSize) {
        this.taskPoolSize = taskPoolSize;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
