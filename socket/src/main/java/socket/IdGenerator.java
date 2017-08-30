package socket;

public class IdGenerator {
    /**
     * 序号
     */
    private static int num = 0;
    /**
     * 实例id
     */
    private static String instanceId = "";

    /**
     * 生成消息id
     *
     * @return
     */
    public synchronized static String genNextId() {
        num = num % 100;
        // String index = (i < 10) ? ("0" + i) : "" + i;
        String index = String.format("%02d", num);
        String orderNum = instanceId + System.currentTimeMillis() + index;
        num++;
        return orderNum;
    }

    public static String genNextId(String instanceId) {
        return instanceId + IdGenerator.genNextId();
    }

}