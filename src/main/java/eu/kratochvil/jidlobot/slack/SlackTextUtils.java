package eu.kratochvil.jidlobot.slack;

import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;

public class SlackTextUtils {

    public static MarkdownTextObject markdownText(String text) {
        return MarkdownTextObject.builder().text(text).build();
    }

    @SuppressWarnings("SameParameterValue")
    public static PlainTextObject plainText(String text) {
        return PlainTextObject.builder().text(text).build();
    }

    /**
     * Builds a text layout block for a Slack message using markdown formatted text.
     *
     * @param text the text to be included in the layout block, which will be formatted as markdown.
     * @return a LayoutBlock containing the provided text formatted as a markdown section.
     */
    public static LayoutBlock buildTextLayoutBlock(String text) {
        return Blocks.section(s -> s.text(SlackTextUtils.markdownText(text)));
    }

    /**
     * Builds a header layout block for a Slack message using plain text.
     *
     * @param header the text to be included in the header layout block, formatted as plain text.
     * @return a LayoutBlock containing the provided header text formatted as a plain text header.
     */
    public static LayoutBlock buildHeaderLayoutBlock(String header) {
        return Blocks.header(h -> h.text(SlackTextUtils.plainText(header)));
    }

}
