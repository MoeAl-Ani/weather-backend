package com.infotamia.weather.pojos.entities;

/**
 * @author Mohammed Al-Ani
 */
public class RoleEntity implements Comparable<RoleEntity> {
    private Integer id;
    private String name;
    private Integer priority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(RoleEntity roleEntity) {
        return this.priority.compareTo(roleEntity.priority) * -1;
    }
}
