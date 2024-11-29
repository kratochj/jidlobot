package eu.kratochvil.jidlobot.slack;

import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMessageBuilder {


    public ChatPostMessageRequest getChatPostHelpMessage() {
        return ChatPostMessageRequest.builder()
                .blocks(buildFormattedHelpMessage())
                .text(buildPlainHelpMessage())
                .build();
    }

    private List<LayoutBlock> buildFormattedHelpMessage() {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(SlackTextUtils.buildHeaderLayoutBlock("Dostupné povely"));
        blocks.add(SlackTextUtils.buildTextLayoutBlock("- @jidlobot menu - vypíše aktuální jídelníček\n- @jidlobot help - tato zpráva"));
        blocks.add(Blocks.divider());
        blocks.add(SlackTextUtils.buildTextLayoutBlock("Denní menu je automaticky stahováno z webových stránek Jídlovic."));
        return blocks;
    }
    private String buildPlainHelpMessage(){
        return "Dostupné příkazy:\n- @jidlobot menu\n- @jidlobot help";
    }

    public ChatPostMessageRequest getChatPostGenericMessage() {
        return ChatPostMessageRequest.builder()
                .blocks(buildFormattedGenericMessage())
                .text("Zadej '@jidlobot help' pro dostupné příkazy.")
                .build();
    }

    private List<LayoutBlock> buildFormattedGenericMessage() {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(SlackTextUtils.buildTextLayoutBlock("Zadej '@jidlobot help' pro dostupné příkazy."));

        return blocks;
    }

}
