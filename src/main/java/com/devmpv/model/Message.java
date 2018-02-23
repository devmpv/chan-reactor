package com.devmpv.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base entity for messages
 *
 * @author devmpv
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Thread thread;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "message_replies",
            joinColumns={@JoinColumn(name="ParentId")},
            inverseJoinColumns={@JoinColumn(name="MessageId")})
    private Set<Message> replies;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "message_replies",
            joinColumns={@JoinColumn(name="MessageId")},
            inverseJoinColumns={@JoinColumn(name="ParentId")})
    private Set<Message> replyTo;

    private String title;

    @Column(nullable = false)
    @Size(min = 1, max = 65535)
    private String text;

    @Column(nullable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "message_attachment")
    private Set<Attachment> attachments;

    public Message() {
        super();
    }

    public Message(String title, String text) {
        setTitle(title);
        setText(text);
        setCreatedAt(System.currentTimeMillis());
        setUpdatedAt(System.currentTimeMillis());
    }

    public Set<Long> getReplyIds() {
        return replies.stream().map(Message::getId).collect(Collectors.toSet());
    }

    public Set<Message> getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Set<Message> replyTo) {
        this.replyTo = replyTo;
    }

    public Set<Message> getReplies() {
        return replies;
    }

    public void setReplies(Set<Message> replies) {
        this.replies = replies;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Thread getThread() {
        return thread;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
