package com.cocos.library_base.entity;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/13
 */
public class FoundModel {


    private int status;
    private String msg;
    private DataBeanX data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        private List<BannerBean> banner;
        private List<NavBean> nav;
        private List<ListBean> list;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<NavBean> getNav() {
            return nav;
        }

        public void setNav(List<NavBean> nav) {
            this.nav = nav;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class BannerBean {


            private String linkUrl;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getEnTitle() {
                return enTitle;
            }

            public void setEnTitle(String enTitle) {
                this.enTitle = enTitle;
            }

            private String title;
            private String enTitle;
            private String imageUrl;

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }

        public static class NavBean {

            private String imageUrl;
            private String enTitle;
            private String title;
            private String linkUrl;

            public int getNavTitleColor() {
                return navTitleColor;
            }

            public void setNavTitleColor(int navTitleColor) {
                this.navTitleColor = navTitleColor;
            }

            private int navTitleColor;

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getEnTitle() {
                return enTitle;
            }

            public void setEnTitle(String enTitle) {
                this.enTitle = enTitle;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }
        }

        public static class ListBean {

            private String header;

            public String getEnHeader() {
                return enHeader;
            }

            public void setEnHeader(String enHeader) {
                this.enHeader = enHeader;
            }

            private String enHeader;
            private List<DataBean> data;

            public String getHeader() {
                return header;
            }

            public void setHeader(String header) {
                this.header = header;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {

                private String title;
                private String enTitle;
                private String enDec;
                private String dec;
                private String linkUrl;
                private String imageUrl;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getEnTitle() {
                    return enTitle;
                }

                public void setEnTitle(String enTitle) {
                    this.enTitle = enTitle;
                }

                public String getEnDec() {
                    return enDec;
                }

                public void setEnDec(String enDec) {
                    this.enDec = enDec;
                }

                public String getDec() {
                    return dec;
                }

                public void setDec(String dec) {
                    this.dec = dec;
                }

                public String getLinkUrl() {
                    return linkUrl;
                }

                public void setLinkUrl(String linkUrl) {
                    this.linkUrl = linkUrl;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }
            }
        }
    }
}
