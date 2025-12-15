package org.acme.utils.objectPost;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LegalDocData {
    private String document_name;
    private String file_path;
    private String type;
    private String document_number;
    private String document_valid_from;
    private String document_valid_until;

    
    
    public String getDocument_name() {
        return document_name;
    }
    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }
    public String getFile_path() {
        return file_path;
    }
    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDocument_number() {
        return document_number;
    }
    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }
    public String getDocument_valid_from() {
        return document_valid_from;
    }
    public void setDocument_valid_from(String document_valid_from) {
        this.document_valid_from = document_valid_from;
    }
    public String getDocument_valid_until() {
        return document_valid_until;
    }
    public void setDocument_valid_until(String document_valid_until) {
        this.document_valid_until = document_valid_until;
    }

    
}
