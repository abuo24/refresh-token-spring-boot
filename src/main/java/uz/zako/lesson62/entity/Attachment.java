package uz.zako.lesson62.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zako.lesson62.entity.base.AbstractEntity;

import javax.persistence.Entity;
import java.io.Serializable;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Attachment extends AbstractEntity implements Serializable {

    private String contentType;

    private Long fileSize;

    private String name;

    private String uploadPath;

    private String hashId;

    private String extension;

}
