package com.messiaen.cryptotoolbox.feature.persistence.entities;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.UrlsDTO;

import java.util.List;

import androidx.room.ColumnInfo;

public class Urls {

    @ColumnInfo
    private List<String> website;

    @ColumnInfo
    private List<String> explorer;

    @ColumnInfo(name = "source_code")
    private List<String> sourceCode;

    @ColumnInfo(name = "message_board")
    private List<String> messageBoard;

    @ColumnInfo
    private List<String> chat;

    @ColumnInfo
    private List<String> announcement;

    @ColumnInfo
    private List<String> reddit;

    @ColumnInfo
    private List<String> twitter;

    public Urls() {
    }

    public Urls(UrlsDTO urls) {
        if (urls == null)
            return;

        this.website = urls.getWebsite();
        this.explorer = urls.getExplorer();
        this.sourceCode = urls.getSourceCode();
        this.messageBoard = urls.getMessageBoard();
        this.chat = urls.getChat();
        this.announcement = urls.getAnnouncement();
        this.reddit = urls.getReddit();
        this.twitter = urls.getTwitter();
    }

    public List<String> getWebsite() {
        return website;
    }

    public void setWebsite(List<String> website) {
        this.website = website;
    }

    public List<String> getExplorer() {
        return explorer;
    }

    public void setExplorer(List<String> explorer) {
        this.explorer = explorer;
    }

    public List<String> getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(List<String> sourceCode) {
        this.sourceCode = sourceCode;
    }

    public List<String> getMessageBoard() {
        return messageBoard;
    }

    public void setMessageBoard(List<String> messageBoard) {
        this.messageBoard = messageBoard;
    }

    public List<String> getChat() {
        return chat;
    }

    public void setChat(List<String> chat) {
        this.chat = chat;
    }

    public List<String> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(List<String> announcement) {
        this.announcement = announcement;
    }

    public List<String> getReddit() {
        return reddit;
    }

    public void setReddit(List<String> reddit) {
        this.reddit = reddit;
    }

    public List<String> getTwitter() {
        return twitter;
    }

    public void setTwitter(List<String> twitter) {
        this.twitter = twitter;
    }
}
