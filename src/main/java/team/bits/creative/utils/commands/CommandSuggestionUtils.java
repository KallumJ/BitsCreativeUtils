package team.bits.creative.utils.commands;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandSuggestionUtils {
    public static final SuggestionProvider<ServerCommandSource> INTEGERS_1_TO_10;

    static {
        INTEGERS_1_TO_10 = (context, builder) -> filterSuggestionsByInput(builder, getListOfIntegers1to10());
    }

    private static CompletableFuture<Suggestions> filterSuggestionsByInput(SuggestionsBuilder builder, Collection<String> values) {
        String start = builder.getRemaining().toLowerCase();
        values.stream().filter(s -> s.toLowerCase().startsWith(start)).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static List<String> getListOfIntegers1to10() {
        List<String> integers = new LinkedList<>();
        for (int i = 1; i <= 10; i++) {
            integers.add(String.valueOf(i));
        }
        return integers;
    }
}
