package eu.mcdb.spicord.logger;

public class JDALogger implements eu.mcdb.internal.org.slf4j.Logger {

    private boolean debug;
    private boolean log;

    public JDALogger(boolean debug, boolean log) {
        this.debug = debug;
        this.log = log;
    }

    @Override
    public void error(Object... obj) {
        log("[ERROR]", obj);
    }

    @Override
    public void warn(Object... obj) {
        log("[WARN]", obj);
    }

    @Override
    public void info(Object... obj) {
        log("[INFO]", obj);
    }

    @Override
    public void debug(Object... obj) {
        if (debug)
            log("[DEBUG]", obj);
    }

    @Override
    public void trace(Object... obj) {
        if (debug)
            log("[TRACE]", obj);
    }

    public void log(String prefix, Object... obj) {
        if (log) {
            String str = String.valueOf(obj[0]);
            for (int i = 1; i < obj.length; i++) {
                str = str.replaceFirst("\\{\\}", String.valueOf(obj[i]));
            }
            System.out.println("[JDA] " + prefix + " " + str);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return debug;
    }
}
