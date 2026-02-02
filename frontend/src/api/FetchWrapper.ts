
export async function apiFetch<T>(url: string, method:string="GET", body:string=null): Promise<T> {
    const res = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: body
    })
  
    if (!res.ok) {
      throw new Error('Error calling API endpoint ' + url)
    }
  
    return res.json()
}
  