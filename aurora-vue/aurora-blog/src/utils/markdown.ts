export default function markdownToHtml(content: any) {
  const MarkdownIt = require('markdown-it')
  const md = new MarkdownIt({
    html: true
  })
    .use(require('markdown-it-katex-external'))
    .use(require('markdown-it-emoji'))
  return md.render(content)
}
