package moe.kira.safe.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SafeAssertion {
    private static final boolean safe;
    static {
        boolean isSafe = false;
        try {
            Field safeField = SafeAssertion.class.getField("safe");
            int modifiers = safeField.getModifiers();
            isSafe = Modifier.isStatic(modifiers) &&
                    Modifier.isFinal(modifiers) &&
                    Modifier.isPrivate(modifiers) &&
                    !safeField.isAccessible() &&
                    verifyServerAndApi() &&
                    verifyClasses();
        } catch (Throwable t) {
            try {
                SafeAssertion.ensuresSafety();
            } catch (Throwable throwable) {
                System.exit(2);
            }
            System.exit(2);
        }
        
        safe = isSafe;
        try {
            Field safeField = SafeAssertion.class.getField("safe");
            int modifiers = safeField.getModifiers();
            isSafe = Modifier.isStatic(modifiers) &&
                    Modifier.isFinal(modifiers) &&
                    Modifier.isPrivate(modifiers) &&
                    !safeField.isAccessible() &&
                    verifyServerAndApi() &&
                    verifyClasses();
            
            boolean fSafe = isSafe;
            Runnable runnable = () -> {
                if (fSafe != safe) {
                    try {
                        SafeAssertion.ensuresSafety();
                    } catch (Throwable t) {
                        System.exit(2);
                    }
                    System.exit(2);
                }
                boolean cache = safe;
                while (safe) {
                    if (!safe || safe != cache || safe != fSafe) {
                        try {
                            SafeAssertion.ensuresSafety();
                        } catch (Throwable t) {
                            System.exit(2);
                        }
                        System.exit(2);
                    }
                    int modifiers_ = safeField.getModifiers();
                    boolean safe = Modifier.isStatic(modifiers_) &&
                                    Modifier.isFinal(modifiers_) &&
                                    Modifier.isPrivate(modifiers_) &&
                                    !safeField.isAccessible() &&
                                    verifyServerAndApi() &&
                                    verifyClasses();
                    if (!safe) {
                        try {
                            SafeAssertion.ensuresSafety();
                        } catch (Throwable t) {
                            System.exit(2);
                        }
                        System.exit(2);
                    }
                }
                if (!safe || safe != cache || safe != fSafe) {
                    try {
                        SafeAssertion.ensuresSafety();
                    } catch (Throwable t) {
                        System.exit(2);
                    }
                    System.exit(2);
                }
            };
            Thread verifyThread = new Thread(runnable);
            verifyThread.setDaemon(true);
            verifyThread.start();
        } catch (Throwable t) {
            try {
                SafeAssertion.ensuresSafety();
            } catch (Throwable throwable) {
                System.exit(2);
            }
            System.exit(2);
        }
    }
    
    private final static List<Object> operators = Lists.newArrayList();
    
    public final static void hook(SafeOperator safeOperation) {
        operators.add(safeOperation);
    }
    
    public final static void hook(Plugin plugin) {
        operators.add(plugin);
    }
    
    private final static void ensuresSafety() {
        operators.forEach(operator -> {
            if (operator instanceof SafeOperator)
                ((SafeOperator) operator).ensuresSafety();
            else
                ((Plugin) operator).onDisable();
        });
        System.exit(2);
    }
    
    private final static boolean verifyClasses() {
        try {
            Class.forName("luohuayu.CatServer");
            Class.forName("catserver.server.CatServer");
            Class.forName("catserver.api.CatServerApi");
        } catch (ClassNotFoundException e) {
            return true;
        }
        return false;
    }
    
    private final static boolean verifyServerAndApi() {
        String serverImplName = Bukkit.getServer().getName().toLowerCase(Locale.ROOT);
        String serverImplVersion = Bukkit.getServer().getVersion().toLowerCase(Locale.ROOT);
        String serverApiVersion = Bukkit.getServer().getBukkitVersion().toLowerCase(Locale.ROOT);
            return !(serverImplName.contains("catserver") || serverImplVersion.contains("catserver") || serverApiVersion.contains("catserver"));
    }
}
