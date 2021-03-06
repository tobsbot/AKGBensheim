package de.tobiaserthal.akgbensheim.backend.rest.model.news;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static android.util.Patterns.WEB_URL;
import de.tobiaserthal.akgbensheim.backend.model.news.NewsModel;
import de.tobiaserthal.akgbensheim.backend.rest.model.base.BaseResponse;

public class NewsResponse extends BaseResponse<NewsResponse.Entry> {

    public NewsResponse(Integer code, String message, List<Entry> data) {
        super(code, message, data);
    }

    public static class Entry implements NewsModel {
        @Expose @SerializedName(NewsKeys.KEY_ID)            private Long id;
        @Expose @SerializedName(NewsKeys.KEY_TITLE)         private String title;
        @Expose @SerializedName(NewsKeys.KEY_ARTICLE)       private String article;
        @Expose @SerializedName(NewsKeys.KEY_ARTICLEURL)    private String articleUrl;
        @Expose @SerializedName(NewsKeys.KEY_IMAGEURL)      private String imageUrl;
        @Expose @SerializedName(NewsKeys.KEY_IMAGEDESC)     private String imageDesc;

        public Entry(Long id, String title, String article, String articleUrl, String imageUrl, String imageDesc) {
            this.id = id;
            this.title = title;
            this.article = article;
            this.articleUrl = articleUrl;
            this.imageUrl = imageUrl;
            this.imageDesc = imageDesc;
        }

        @NonNull
        @Override
        public String getTitle() {
            return title;
        }

        @NonNull
        @Override
        public String getSnippet() {
            String condensed = getArticle().replace("\r\n", " ");
            return condensed.length() > 150 ? condensed.substring(0, 150) + "..." : condensed;
        }

        @NonNull
        @Override
        public String getArticle() {
            return article != null ? article : "";
        }

        @NonNull
        @Override
        public String getArticleUrl() {
            return articleUrl;
        }

        @Nullable
        @Override
        public String getImageUrl() {
            return imageUrl;
        }

        @Nullable
        @Override
        public String getImageDesc() {
            return imageDesc;
        }

        @Override
        public boolean getBookmarked() {
            return false;
        }

        @Override
        public boolean hasImage() {
            return imageUrl != null
                    && WEB_URL.matcher(imageUrl).matches();
        }

        @Override
        public boolean hasImageDes() {
            return !TextUtils.isEmpty(imageDesc);
        }

        @Override
        public long getId() {
            return id;
        }
    }
}
