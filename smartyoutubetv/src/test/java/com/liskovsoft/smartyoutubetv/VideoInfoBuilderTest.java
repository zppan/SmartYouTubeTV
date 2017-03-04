package com.liskovsoft.smartyoutubetv;

import com.liskovsoft.smartyoutubetv.helpers.VideoFormat;
import com.liskovsoft.smartyoutubetv.helpers.VideoInfoBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VideoInfoBuilderTest {
    private InputStream mOriginalInfo;
    private InputStream mModifiedInfo;
    private InputStream mFullHDInfo;

    @Before
    public void setUp() throws Exception {
        mOriginalInfo = TestHelpers.openResource("get_video_info_origin");
        mModifiedInfo = TestHelpers.openResource("get_video_info_modified");
        mFullHDInfo = TestHelpers.openResource("get_video_info_full_hd");
    }

    @Test
    public void testFormatsRemoving() throws Exception {
        VideoInfoBuilder builder = new VideoInfoBuilder(mOriginalInfo);
        builder.removeFormat(248); // webm 1920x1080
        builder.removeFormat(137); // mp4 1920x1080
        InputStream result = builder.get();

        assertTrue(IOUtils.contentEquals(result, mModifiedInfo));
    }

    @Test
    public void testSelectFormat() throws IOException {
        VideoInfoBuilder builder = new VideoInfoBuilder(mOriginalInfo);
        builder.selectFormat(VideoFormat._1080p_);
        InputStream result = builder.get();

        assertTrue(IOUtils.contentEquals(result, mFullHDInfo));
    }

    @Test
    public void testGetAllSupportedFormats() {
        Set<VideoFormat> testFormats = createTestFormats();

        VideoInfoBuilder builder = new VideoInfoBuilder(mOriginalInfo);
        Set<VideoFormat> allSupportedFormats = builder.getSupportedFormats();
        testFormats.removeAll(allSupportedFormats);

        assertTrue(testFormats.isEmpty());
    }

    private Set<VideoFormat> createTestFormats() {
        Set<VideoFormat> testFormats = new HashSet<>();
        testFormats.add(VideoFormat._144p_);
        testFormats.add(VideoFormat._240p_);
        testFormats.add(VideoFormat._360p_);
        testFormats.add(VideoFormat._480p_);
        testFormats.add(VideoFormat._720p_);
        testFormats.add(VideoFormat._1080p_);
        return testFormats;
    }
}