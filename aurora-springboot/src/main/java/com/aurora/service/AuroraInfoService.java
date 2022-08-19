package com.aurora.service;

import com.aurora.dto.AboutDTO;
import com.aurora.dto.AuroraAdminInfoDTO;
import com.aurora.dto.AuroraHomeInfoDTO;
import com.aurora.dto.WebsiteConfigDTO;
import com.aurora.vo.AboutVO;
import com.aurora.vo.WebsiteConfigVO;

public interface AuroraInfoService {

    void report();

    AuroraHomeInfoDTO getAuroraHomeInfo();

    AuroraAdminInfoDTO getAuroraAdminInfo();

    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    WebsiteConfigDTO getWebsiteConfig();

    void updateAbout(AboutVO aboutVO);

    AboutDTO getAbout();


}
