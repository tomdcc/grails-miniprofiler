import com.energizedwork.miniprofiler.MiniProfiler

class MiniProfilerTagLib {

    static namespace = "miniprofiler"

	def bufferedAppender
	def grailsApplication
	def pluginManager
	def profilerProvider

    def javascript = { attrs ->
        MiniProfiler profiler = profilerProvider.current
        if (!profiler) return

        def path = g.resource(dir: 'js', plugin: 'miniprofiler') + '/'
        def plugin = pluginManager.getGrailsPlugin('miniprofiler')
        def version = plugin.version
//        ids = ids.ToJson(),
        def ids = profiler.id
//        position = (position ?? MiniProfiler.Settings.PopupRenderPosition).ToString().ToLower(),
        def position = "left"
//        showTrivial = showTrivial ?? MiniProfiler.Settings.PopupShowTrivial ? "true" : "false",
        def showTrivial = true
//        showChildren = showTimeWithChildren ?? MiniProfiler.Settings.PopupShowTimeWithChildren ? "true" : "false",
        def showChildren = false
//        maxTracesToShow = maxTracesToShow ?? MiniProfiler.Settings.PopupMaxTracesToShow,
        def maxTracesToShow = 15
//        showControls = showControls ?? MiniProfiler.Settings.ShowControls ? "true" : "false",
        def showControls = false
//        currentId = profiler.Id,
        def currentId = profiler.id
//        authorized = authorized ? "true" : "false",
        def authorized = true
//        useExistingjQuery = useExistingjQuery ?? MiniProfiler.Settings.UseExistingjQuery ? "true" : "false"
        def useExistingjQuery = false

        out << """<script async type="text/javascript" id="mini-profiler" src="${path}includes.js?v=${version}" data-version="${version}" data-path="${path}" data-current-id="${currentId}" data-ids="${ids}" data-position="${position}" data-trivial="${showTrivial}" data-children="${showChildren}" data-max-traces="${maxTracesToShow}" data-controls="${showControls}" data-authorized="${authorized}"></script>"""

    }
}
