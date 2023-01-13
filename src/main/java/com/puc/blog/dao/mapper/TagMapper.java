package com.puc.blog.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puc.blog.dao.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id 查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);


    /**
     * 查询最热的标签  前n条
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);


    /**
     * 根据tagId 查询 tagName
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}

