package ocean_article.dao;

import ocean_article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleDao extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {

    @Modifying
    @Query(value = "update tb_article set state=1 where id=?",nativeQuery = true)
    public void examine(String id);

    @Modifying
    @Query(value = "update tb_article set thumbmp=thumbmp+1 where id=?",nativeQuery = true)
    public void updateThumbmp(String id);
}
