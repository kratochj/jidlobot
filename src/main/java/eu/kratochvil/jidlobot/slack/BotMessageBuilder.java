package eu.kratochvil.jidlobot.slack;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMessageBuilder {


    public ChatPostMessageRequest getChatPostHelpMessage(boolean isIm) {
        return ChatPostMessageRequest.builder()
                .blocks(buildFormattedHelpMessage(isIm))
                .text(buildPlainHelpMessage(isIm))
                .build();
    }

    private List<LayoutBlock> buildFormattedHelpMessage(boolean isIm) {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(SlackTextUtils.buildHeaderLayoutBlock("Dostupné povely"));
        if (isIm) {
            blocks.add(SlackTextUtils.buildTextLayoutBlock("- `menu` - vypíše aktuální jídelníček\n- `help` - tato zpráva"));
        } else {
            blocks.add(SlackTextUtils.buildTextLayoutBlock("- `@jidlobot menu` - vypíše aktuální jídelníček\n- `@jidlobot help` - tato zpráva"));
        }
        blocks.add(Blocks.divider());
        blocks.add(SlackTextUtils.buildTextLayoutBlock("Denní menu je automaticky stahováno z webových stránek Jídlovic."));
        return blocks;
    }

    private String buildPlainHelpMessage(boolean isIm) {
        if (isIm)
            return "Dostupné příkazy:\n- `menu`\n- `help`";
        else
            return "Dostupné příkazy:\n- `@jidlobot menu`\n- `@jidlobot help`";
    }

    public ChatPostMessageRequest getChatPostGenericMessage(boolean isIm) {
        if (isIm)
            return ChatPostMessageRequest.builder()
                    .blocks(buildFormattedGenericMessage(isIm))
                    .text("Zadej `help` pro dostupné příkazy.")
                    .build();
        else
            return ChatPostMessageRequest.builder()
                    .blocks(buildFormattedGenericMessage(isIm))
                    .text("Zadej `@jidlobot help` pro dostupné příkazy.")
                    .build();
    }

    private List<LayoutBlock> buildFormattedGenericMessage(boolean isIm) {
        List<LayoutBlock> blocks = new ArrayList<>();
        if (isIm)
            blocks.add(SlackTextUtils.buildTextLayoutBlock("Zadej `help` pro dostupné příkazy."));
        else
            blocks.add(SlackTextUtils.buildTextLayoutBlock("Zadej `@jidlobot help` pro dostupné příkazy."));


        return blocks;
    }

}
