import mermaidPlugin from "@agoose77/markdown-it-mermaid";
export default function markdownToHtml(content: any) {
  const MarkdownIt = require('markdown-it')
  const md = new MarkdownIt({
    html: true
  })
    .use(require('markdown-it-katex-external'))
    .use(require('markdown-it-emoji'))
    .use(require('markdown-it-container'), 'hljs-center')  // 容器插件
    .use(require('markdown-it-container'), 'hljs-left')
    .use(require('markdown-it-container'), 'hljs-right')
    .use(require('markdown-it-sup'))                       // 上角标插件
    .use(require('markdown-it-sub'))                       // 下角标插件
    .use(require('markdown-it-footnote'))                  // 脚注插件
    .use(require('markdown-it-abbr'))                      // 缩写插件
    .use(require('markdown-it-ins'))                       // 插入插件
    .use(require('markdown-it-mark'))                      // 标记插件
    .use(require('@iktakahiro/markdown-it-katex'))
    .use(mermaidPlugin)
  return md.render(content)
}
