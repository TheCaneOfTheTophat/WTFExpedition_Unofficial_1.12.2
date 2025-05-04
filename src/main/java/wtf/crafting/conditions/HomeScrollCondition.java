package wtf.crafting.conditions;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import wtf.config.WTFExpeditionConfig;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class HomeScrollCondition implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> WTFExpeditionConfig.homeScrollsEnabled;
    }
}
