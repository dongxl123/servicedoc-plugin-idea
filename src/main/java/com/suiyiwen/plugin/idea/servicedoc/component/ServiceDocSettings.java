package com.suiyiwen.plugin.idea.servicedoc.component;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 15:12
 */
@State(
        name = "ServiceDocSettings",
        storages = @Storage(value = "servicedoc.xml")
)
public class ServiceDocSettings implements PersistentStateComponent<ServiceDocSettings> {

    private String version;

    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static ServiceDocSettings getInstance() {
        return ServiceManager.getService(ServiceDocSettings.class);
    }

    @Nullable
    @Override
    public ServiceDocSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ServiceDocSettings state) {
        this.version = state.getVersion();
        this.author = state.getAuthor();
    }
    
}
