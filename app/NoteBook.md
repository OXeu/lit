记录几个WebView方法
zoomBy zoomIn zoomOut 缩放WebView
2303    /**
2304     * Performs a zoom operation in this WebView.
2305     *
2306     * @param zoomFactor the zoom factor to apply. The zoom factor will be clamped to the WebView's
2307     * zoom limits. This value must be in the range 0.01 to 100.0 inclusive.
2308     */
2309    public void zoomBy(float zoomFactor) {
2310        checkThread();
2311        if (zoomFactor < 0.01)
2312            throw new IllegalArgumentException("zoomFactor must be greater than 0.01.");
2313        if (zoomFactor > 100.0)
2314            throw new IllegalArgumentException("zoomFactor must be less than 100.");
2315        mProvider.zoomBy(zoomFactor);
2316    }
2317
2318    /**
2319     * Performs zoom in in this WebView.
2320     *
2321     * @return {@code true} if zoom in succeeds, {@code false} if no zoom changes
2322     */
2323    public boolean zoomIn() {
2324        checkThread();
2325        return mProvider.zoomIn();
2326    }
2327
2328    /**
2329     * Performs zoom out in this WebView.
2330     *
2331     * @return {@code true} if zoom out succeeds, {@code false} if no zoom changes
2332     */
2333    public boolean zoomOut() {
2334        checkThread();
2335        return mProvider.zoomOut();
2336    }